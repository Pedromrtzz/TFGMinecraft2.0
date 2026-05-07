package com.pedromrtz.tfgmod.quiz;

import java.util.List;

public record QuizQuestion(
        String question,
        List<String> answers,
        int correctIndex
) {}