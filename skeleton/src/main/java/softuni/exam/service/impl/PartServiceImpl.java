package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartImportDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_PART;
import static softuni.exam.constants.Messages.VALID_PART_INPUT;
import static softuni.exam.constants.Paths.PARTS_JSON_PATH;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final StringBuilder sb;
    private final ValidationUtils validationUtils;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, Gson gson, ModelMapper modelMapper, StringBuilder sb, ValidationUtils validationUtils) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.sb = sb;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(PARTS_JSON_PATH);
    }

    @Override
    public String importParts() throws IOException {
        final List<PartImportDto> parts = Arrays.stream(gson.fromJson(readPartsFileContent(), PartImportDto[].class)).toList();

        for (PartImportDto part : parts) {
            boolean isValid = this.validationUtils.isValid(part);

            if (this.partRepository.findFirstByPartName(part.getPartName()).isPresent()) {
                continue;
            }

            if(isValid) {
                Part partToSave = this.modelMapper.map(part, Part.class);
                this.partRepository.saveAndFlush(partToSave);

                sb.append(String.format(VALID_PART_INPUT, part.getPartName(), part.getPrice()));
            } else {
                sb.append(INVALID_PART).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
