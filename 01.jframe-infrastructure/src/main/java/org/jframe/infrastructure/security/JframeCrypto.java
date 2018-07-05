package org.jframe.infrastructure.security;

import org.jframe.core.app.AppInitializer;
import org.jframe.core.security.Crypto;

/**
 * Created by Leo on 2017/1/8.
 */
public class JframeCrypto extends Crypto implements AppInitializer {

    private final static JframeCrypto instance = new JframeCrypto();

    public static JframeCrypto getInstance() {
        return instance;
    }

    private JframeCrypto() {
        super("W5pK0o", "DES");
    }

    @Override
    public String init() {
        super.initialize();
        return this.getClass().getName() + " initialize success!";
    }

    @Override
    public void close() {

    }
}
