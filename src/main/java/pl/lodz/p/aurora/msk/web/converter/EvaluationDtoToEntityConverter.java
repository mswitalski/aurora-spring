package pl.lodz.p.aurora.msk.web.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.msk.web.dto.EvaluationDto;
import pl.lodz.p.aurora.msk.domain.entity.Evaluation;
import pl.lodz.p.aurora.msk.domain.entity.Skill;
import pl.lodz.p.aurora.mus.domain.entity.User;

public class EvaluationDtoToEntityConverter implements Converter<EvaluationDto, Evaluation> {

    private final TypeMap<EvaluationDto, Evaluation> typeMap;
    private final ModelMapper mapper = new ModelMapper();

    public EvaluationDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(EvaluationDto.class, Evaluation.class)
                .addMappings(mapper -> mapper.skip(Evaluation::setSkill))
                .addMappings(mapper -> mapper.skip(Evaluation::setUser));
    }

    @Override
    public Evaluation convert(EvaluationDto evaluation) {
        Evaluation convertedEvaluation = typeMap.map(evaluation);
        convertedEvaluation.setSkill(mapper.map(evaluation.getSkill(), Skill.class));

        if (evaluation.getUser() != null) {
            convertedEvaluation.setUser(mapper.map(evaluation.getUser(), User.class));
        }

        return convertedEvaluation;
    }

}