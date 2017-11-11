package com.coamctech.bxloan.manager.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * Created by Administrator on 2017/11/11.
 */
public class MyNamingStrategy extends SpringPhysicalNamingStrategy {
    private static final String TABLE_PREFIX="t_";
    @Override
    public Identifier toPhysicalTableName(Identifier name,
                                          JdbcEnvironment jdbcEnvironment) {
        Identifier originName = super.toPhysicalTableName(name, jdbcEnvironment);
        return new Identifier(TABLE_PREFIX+originName.getText(),originName.isQuoted());
    }
}
