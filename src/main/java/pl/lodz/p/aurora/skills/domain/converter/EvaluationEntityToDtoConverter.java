package pl.lodz.p.aurora.skills.domain.converter;

import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.common.domain.converter.BaseConverter;
import pl.lodz.p.aurora.skills.domain.dto.EvaluationDto;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;
import pl.lodz.p.aurora.users.domain.converter.UserBasicDtoConverter;

public class EvaluationEntityToDtoConverter extends BaseConverter implements Converter<Evaluation, EvaluationDto> {

    private final TypeMap<Evaluation, EvaluationDto> typeMap;

    public EvaluationEntityToDtoConverter() {
        typeMap = mapper.createTypeMap(Evaluation.class, EvaluationDto.class)
                .addMappings(mapper -> mapper.skip(EvaluationDto::setSkill))
                .addMappings(mapper -> mapper.skip(EvaluationDto::setUser));
    }

    @Override
    public EvaluationDto convert(Evaluation evaluation) {
        EvaluationDto convertedEvaluation = typeMap.map(evaluation);
        SkillBasicDtoConverter skillConverter = new SkillBasicDtoConverter();
        UserBasicDtoConverter userConverter = new UserBasicDtoConverter();

        convertedEvaluation.setSkill(skillConverter.convert(evaluation.getSkill()));
        convertedEvaluation.setUser(userConverter.convert(evaluation.getUser()));

        return convertedEvaluation;
    }

}