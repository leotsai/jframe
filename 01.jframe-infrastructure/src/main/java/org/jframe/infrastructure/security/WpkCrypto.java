package org.jframe.infrastructure.security;

import org.jframe.core.security.Crypto;

/**
 * Created by Leo on 2017/1/8.
 */
public class WpkCrypto extends Crypto {

    private final static WpkCrypto instance = new WpkCrypto();

    public static WpkCrypto getInstance() {
        return instance;
    }

    private WpkCrypto() {
        super("W5pK0o", "DES");
    }

}
