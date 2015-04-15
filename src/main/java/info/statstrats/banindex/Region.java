/**
 * Copyright (c) 2015 Desmond Silveira. All rights reserved.
 */
package info.statstrats.banindex;

/**
 * @author dsilveira
 */
public enum Region {
    BR("br"),
    EUNE("eune"),
    EUW("euw"),
    KR("kr"),
    LAN("lan"),
    LAS("las"),
    NA("na"),
    OCE("oce"),
    RU("ru"),
    TR("tr")
    ;

    final String name;

    Region(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
