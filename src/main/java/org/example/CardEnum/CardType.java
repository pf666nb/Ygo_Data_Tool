package org.example.CardEnum;

public enum CardType {
    MONSTER(0,"MONSTER"),
    SPELL(1,"SPELL"),
    TRAP(2,"TRAP"),
    DECK(3,"DECK"),
    EX(4,"EX"),
    SIDE(5,"SIDE");


    private String card;
    private Integer type;
    private CardType(Integer type,String card){
        this.type = type;
        this.card  =card;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
