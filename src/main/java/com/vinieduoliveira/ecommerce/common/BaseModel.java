package com.vinieduoliveira.ecommerce.common;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

//Every entity inherits from it
public class BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseModel that = (BaseModel) obj;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
