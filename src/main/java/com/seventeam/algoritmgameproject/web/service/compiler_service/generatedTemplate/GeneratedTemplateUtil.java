package com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneratedTemplateUtil {

    public void variableLoop(StringBuilder out, List<?> varList){
        for (int i = 0; i < varList.size(); i++) {
            if (i == varList.size() - 1) {
                out.append(varList.get(i));
            } else {
                out.append(varList.get(i)).append(",");
            }
        }
    }
}
