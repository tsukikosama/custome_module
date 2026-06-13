/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.common.config.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.piped.FromQuery;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.TableStatement;
import net.sf.jsqlparser.statement.select.Values;
import net.sf.jsqlparser.statement.select.WithItem;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;

/**
 * 逻辑删除 SQL 拦截器
 * <p>
 * 为自定义 Mapper XML 中的 SELECT 查询自动注入 deleted = 0 条件，
 * 避免在每处 XML 中手动编写。
 * </p>
 *
 * @author Charles7c
 * @since 2025/6/11
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class LogicDeleteInterceptor extends JsqlParserSupport implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String originalSql = boundSql.getSql();

        // 只处理 SELECT 语句
        MappedStatement mappedStatement = getMappedStatement(statementHandler);
        if (mappedStatement == null || mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            return invocation.proceed();
        }

        // 如果 SQL 中已包含 deleted 条件，跳过
        if (StrUtil.containsIgnoreCase(originalSql, "deleted")) {
            return invocation.proceed();
        }

        try {
            String modifiedSql = this.parserSingle(originalSql, null);
            // 通过反射替换 BoundSql 中的 SQL
            Field sqlField = BoundSql.class.getDeclaredField("sql");
            sqlField.setAccessible(true);
            sqlField.set(boundSql, modifiedSql);
        } catch (Exception e) {
            // 解析失败则执行原 SQL，不阻塞业务
        }

        return invocation.proceed();
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        Select selectBody = select.getSelectBody();
        if (selectBody != null) {
            selectBody.accept(selectVisitorAdapter, null);
        }
    }

    private final SelectVisitor<Void> selectVisitorAdapter = new SelectVisitor<>() {
        @Override
        public <S> Void visit(PlainSelect plainSelect, S context) {
            Expression where = plainSelect.getWhere();
            EqualsTo deletedCondition = new EqualsTo();
            deletedCondition.setLeftExpression(new Column("deleted"));
            deletedCondition.setRightExpression(new LongValue(0));
            if (where == null) {
                plainSelect.setWhere(deletedCondition);
            } else {
                plainSelect.setWhere(new AndExpression(where, deletedCondition));
            }
            return null;
        }

        @Override
        public <S> Void visit(SetOperationList setOpList, S context) {
            for (Select select : setOpList.getSelects()) {
                select.accept(this, context);
            }
            return null;
        }

        @Override
        public <S> Void visit(WithItem<?> withItem, S context) {
            withItem.getSelect().accept(this, context);
            return null;
        }

        @Override
        public <S> Void visit(ParenthesedSelect parenthesedSelect, S context) {
            parenthesedSelect.getSelect().accept(this, context);
            return null;
        }

        @Override
        public <S> Void visit(Values values, S context) {
            return null;
        }

        @Override
        public <S> Void visit(LateralSubSelect lateralSubSelect, S context) {
            return null;
        }

        @Override
        public <S> Void visit(TableStatement tableStatement, S context) {
            return null;
        }

        @Override
        public <S> Void visit(FromQuery fromQuery, S context) {
            return null;
        }
    };

    /**
     * 从 StatementHandler 获取 MappedStatement
     */
    private MappedStatement getMappedStatement(StatementHandler handler) {
        try {
            Field field = handler.getClass().getDeclaredField("mappedStatement");
            field.setAccessible(true);
            return (MappedStatement)field.get(handler);
        } catch (Exception e) {
            return null;
        }
    }
}
