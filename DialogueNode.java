package com.mycompany.mygame;

public class DialogueNode {
    public String speaker;
    public String text;
    public String[] choices;
    public int[] choiceNext;
    public int nextNode;

    public DialogueNode(String speaker, String text, String[] choices, int[] choiceNext, int nextNode) {
        this.speaker = speaker;
        this.text = text;
        this.choices = choices;
        this.choiceNext = choiceNext;
        this.nextNode = nextNode;
    }
}