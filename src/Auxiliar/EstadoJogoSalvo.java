package Auxiliar;

import Modelo.fases.Fase;
import java.io.Serializable;

public class EstadoJogoSalvo implements Serializable {
    private static final long serialVersionUID = 1L;
    public int faseAtualIndex;
    public Fase faseObject;
    public int mundoLargura;
    public int mundoAltura;

    public EstadoJogoSalvo(int faseAtualIndex, Fase faseObject, int mundoLargura, int mundoAltura) {
        this.faseAtualIndex = faseAtualIndex;
        this.faseObject = faseObject;
        this.mundoLargura = mundoLargura;
        this.mundoAltura = mundoAltura;
    }

    public int getFaseAtualIndex() {
        return faseAtualIndex;
    }

    public Fase getFaseObject() {
        return faseObject;
    }

    public int getMundoLargura() {
        return mundoLargura;
    }

    public int getMundoAltura() {
        return mundoAltura;
    }
}