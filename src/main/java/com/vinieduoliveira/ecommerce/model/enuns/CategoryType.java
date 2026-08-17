package com.vinieduoliveira.ecommerce.model.enuns;

public enum CategoryType {
    ELETRONICOS(1),
    ALIMENTOS(2),
    VESTUARIO(3),
    MOVEIS(4),
    INFORMATICA(5),
    ESPORTES(6),
    BELEZA(7),
    BRINQUEDOS(8);

    private final int code;

    private CategoryType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CategoryType valueOf(int code) {
        for (CategoryType value : CategoryType.values()) {
            if ( value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid CategoryType code");
    }
}