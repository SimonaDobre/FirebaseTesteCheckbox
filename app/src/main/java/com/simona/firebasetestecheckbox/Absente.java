package com.simona.firebasetestecheckbox;

public class Absente {

    private String idulAbse;
    private String dataAbs;
    private String materiaAbs;
    private String idul;
    private static final String STA = "NE";
    private String status = STA;
    private String numele;

    public Absente(String idulAbse, String dataAbs, String materiaAbs, String idul, String status, String numele) {
        this.idulAbse = idulAbse;
        this.dataAbs = dataAbs;
        this.materiaAbs = materiaAbs;
        this.idul = idul;
        this.status = status;
        this.numele = numele;
    }

    public Absente(String dataAbs, String materiaAbs) {
        this.dataAbs = dataAbs;
        this.materiaAbs = materiaAbs;
    }


    public String getNumele() {
        return numele;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void schimbMotivataNemotivata() {
        if (status.equals("NE")) {
            status = "MOTI";
        } else {
            status = "NE";
        }
    }


    public String getIdul() {
        return idul;
    }

    public String getIdulAbse() {
        return idulAbse;
    }

    public String getStatus() {
        return status;
    }

    public Absente() {
    }

    public String getDataAbs() {
        return dataAbs;
    }

    public String getMateriaAbs() {
        return materiaAbs;
    }


}
