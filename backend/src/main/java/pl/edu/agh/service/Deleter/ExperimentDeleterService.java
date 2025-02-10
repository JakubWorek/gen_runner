package pl.edu.agh.service.Deleter;

import com.mongodb.MongoCommandException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import pl.edu.agh.dto.DeleteResult;
import pl.edu.agh.exception.ExperimentGroupNotFoundException;
import pl.edu.agh.exception.ExperimentNotFoundException;
import pl.edu.agh.repository.ExperimentGroupRepository;
import pl.edu.agh.repository.ExperimentRepository;
import pl.edu.agh.repository.ExperimentResultRepository;
import pl.edu.agh.repository.MongoTransactionRunner;

import java.util.Optional;


@Service
public class ExperimentDeleterService {

    private ExperimentRepository experimentRepository;
    private ExperimentResultRepository resultRepository;
    private ExperimentGroupRepository groupRepository;
    private MongoTransactionRunner transactionRunner;

    public ExperimentDeleterService(ExperimentRepository experimentRepository, ExperimentResultRepository resultRepository, ExperimentGroupRepository groupRepository, MongoTransactionRunner transactionRunner) {
        this.experimentRepository = experimentRepository;
        this.resultRepository = resultRepository;
        this.groupRepository = groupRepository;
        this.transactionRunner = transactionRunner;
    }

    public DeleteResult deleteExperimentGroup(String groupName) {
        if(groupRepository.findByGroupName(groupName) == null) {
            return new DeleteResult(false, 400, Optional.of(new ExperimentGroupNotFoundException(groupName)));
        }
        try {
            groupRepository.deleteByGroupName(groupName);
            return new DeleteResult(true, 204, Optional.empty());
        } catch (MongoCommandException e) {
            return new DeleteResult(false, 500, Optional.of(e));
        }
    }

    public DeleteResult deleteExperiment(ObjectId id) {
        if (!experimentRepository.existsById(id)) {
            return new DeleteResult(false, 400, Optional.of(new ExperimentNotFoundException(id)));
        }

        try {
            transactionRunner.runWithTransaction(() -> deleteById(id));
            return new DeleteResult(true, 204, Optional.empty());
        } catch (MongoCommandException e) {
            return new DeleteResult(false, 500, Optional.of(e));
        }
    }

    private void deleteById(ObjectId id) {
        experimentRepository.deleteById(id);

        resultRepository.findByExperimentId(id).forEach(result ->
            resultRepository.deleteById(result.getId())
        );
    }
}
