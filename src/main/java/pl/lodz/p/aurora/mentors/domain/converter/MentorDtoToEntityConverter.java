package pl.lodz.p.aurora.mentors.domain.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import pl.lodz.p.aurora.mentors.domain.dto.MentorDto;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.skills.domain.entity.Evaluation;

public class MentorDtoToEntityConverter implements Converter<MentorDto, Mentor> {

    private final TypeMap<MentorDto, Mentor> typeMap;
    private final ModelMapper mapper = new ModelMapper();

    public MentorDtoToEntityConverter() {
        typeMap = mapper.createTypeMap(MentorDto.class, Mentor.class)
                .addMappings(mapper -> mapper.skip(Mentor::setEvaluation));
    }

    @Override
    public Mentor convert(MentorDto dto) {
        Mentor convertedMentor = typeMap.map(dto);
        convertedMentor.setEvaluation(mapper.map(dto.getEvaluation(), Evaluation.class));

        return convertedMentor;
    }
}
