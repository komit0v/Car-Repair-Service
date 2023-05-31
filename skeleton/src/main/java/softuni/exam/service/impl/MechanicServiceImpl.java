package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicImportDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_MECHANIC;
import static softuni.exam.constants.Messages.VALID_MECHANIC_INPUT;
import static softuni.exam.constants.Paths.MECHANICS_JSON_PATH;

@Service
public class MechanicServiceImpl implements MechanicService {
    private final MechanicRepository mechanicRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final StringBuilder sb;
    private final ValidationUtils validationUtils;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository, Gson gson, ModelMapper modelMapper, StringBuilder sb, ValidationUtils validationUtils) {
        this.mechanicRepository = mechanicRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.sb = sb;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {

        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(MECHANICS_JSON_PATH);
    }

    @Override
    public String importMechanics() throws IOException {

        final List<MechanicImportDto> mechanics = Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicImportDto[].class)).toList();

        for (MechanicImportDto mechanic : mechanics) {
            boolean isValid = this.validationUtils.isValid(mechanic);

            if (this.mechanicRepository.findFirstByEmail(mechanic.getEmail()).isPresent()) {
                continue;
            }

            if (isValid) {
                Mechanic mechanicToSave = this.modelMapper.map(mechanic, Mechanic.class);
                this.mechanicRepository.saveAndFlush(mechanicToSave);

                sb.append(String.format(VALID_MECHANIC_INPUT, mechanic.getFirstName(), mechanic.getLastName()));
            } else {
                sb.append(INVALID_MECHANIC).append(System.lineSeparator());
            }

        }

        return sb.toString();
    }
}
