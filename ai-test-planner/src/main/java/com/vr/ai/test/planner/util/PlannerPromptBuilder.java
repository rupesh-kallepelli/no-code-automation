package com.vr.ai.test.planner.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vr.ai.test.planner.model.testcase.TrainingPair;

@Component
public class PlannerPromptBuilder {

    public String build(String nl, List<TrainingPair> examples) {

        StringBuilder ex = new StringBuilder();

        for (TrainingPair p : examples) {
            ex.append("NL:\n")
                    .append(p.getNl())
                    .append("\n\nJSON:\n")
                    .append(p.getJson())
                    .append("\n\n---\n\n");
        }

        return """
                You are an AI UI automation planner.

                Convert NL into TestCase JSON.

                Rules:
                - Preserve NL exactly
                - Use valid ActionType
                - Use valid Identifier types
                - Infer implicit steps
                - Use ${username} and ${password}
                - Preserve URLs

                EXAMPLES:
                %s

                NL:
                %s

                Return STRICT JSON:
                {
                  "nl": "<original NL>",
                  "json": { TestCase }
                }
                """.formatted(ex.toString(), nl);
    }
}