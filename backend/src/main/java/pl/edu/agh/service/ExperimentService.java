package pl.edu.agh.service;

import org.bson.types.ObjectId;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.moeaframework.core.indicator.StandardIndicator;
import org.springframework.stereotype.Service;
import pl.edu.agh.dto.AggregatedExperimentDto;
import pl.edu.agh.dto.DeleteResult;
import pl.edu.agh.dto.SingleRunExperimentResult;
import pl.edu.agh.model.Experiment;
import pl.edu.agh.service.Aggregator.ExperimentAggregatorService;
import pl.edu.agh.service.Deleter.ExperimentDeleterService;
import pl.edu.agh.service.Group.ExperimentGroupingService;
import pl.edu.agh.service.Result.ExperimentResultService;
import pl.edu.agh.service.Result.FilterCriterion;
import pl.edu.agh.service.Runner.ExperimentRunnerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExperimentService {

    private final ExperimentRunnerService runnerService;
    private final ExperimentAggregatorService aggregatorService;
    private final ExperimentResultService resultService;
    private final ExperimentDeleterService deleterService;

    private final ExperimentGroupingService groupingService;


    public ExperimentService(ExperimentRunnerService runnerService, ExperimentAggregatorService aggregatorService, ExperimentResultService resultService, ExperimentDeleterService deleterService, ExperimentGroupingService groupingService) {
        this.runnerService = runnerService;
        this.aggregatorService = aggregatorService;
        this.resultService = resultService;
        this.deleterService = deleterService;
        this.groupingService = groupingService;
    }

    public List<AggregatedExperimentDto> aggregateExperiments(Instant startDate, Instant endDate,
                                                              List<String> algorithms,
                                                              List<String> problems,
                                                              List<String> metrics,
                                                              String groupName) {
        return aggregatorService.aggregateExperiments(startDate, endDate, algorithms, problems, metrics, groupName);
    }


    public ObjectId runExperiments(Set<String> algorithms, Set<String> problems, Set<StandardIndicator> metrics, int budget, int runs) {
        return runnerService.runExperiments(algorithms, problems, metrics, budget, runs);
    }

    public Experiment getExperiment(ObjectId id) {
        return resultService.getExperiment(id);
    }

    public List<Experiment> listExperiments() {
        return resultService.listExperiments();
    }

    public List<Experiment> listExperimentsWithFilters(List<FilterCriterion> filters, String groupName) {
        return resultService.listExperimentsWithFilters(filters, groupName);
    }

    public List<SingleRunExperimentResult> getResults(ObjectId experimentId) {
        return resultService.getResults(experimentId);
    }

    public DeleteResult deleteExperiment(ObjectId experimentId) {
        return deleterService.deleteExperiment(experimentId);
    }

    //TODO change returned type
    public void groupExperiments(String groupName, List<String> experimentsIdToAdd){
        groupingService.groupExperiments(groupName,experimentsIdToAdd);
    }

    public List<Experiment> getGroupedExperiments(String groupName){
        return groupingService.getGroupedExperiments(groupName);
    }

    public DeleteResult deleteExperimentsGroup(String groupName) {
        return deleterService.deleteExperimentGroup(groupName);
    }

    public List<AggregatedExperimentDto> getAggregatedResults(
            String startDate,
            String endDate,
            List<String> algorithms,
            List<String> problems,
            List<String> metrics,
            String groupName
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        Instant parsedStartDate = startDate != null ? Instant.from(formatter.parse(startDate)) : null;
        Instant parsedEndDate = endDate != null ? Instant.from(formatter.parse(endDate)) : null;

        return aggregateExperiments(parsedStartDate, parsedEndDate, algorithms, problems, metrics, groupName);
    }

    public String generateCsvContent(
            String startDate,
            String endDate,
            List<String> algorithms,
            List<String> problems,
            List<String> metrics,
            String groupName
    ) {
        List<AggregatedExperimentDto> aggregatedResults = getAggregatedResults(
                startDate, endDate, algorithms, problems, metrics, groupName
        );

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Algorithm,Problem,Iteration,Indicator,Average,Median,StandardDeviation,Count\n");
        for (AggregatedExperimentDto result : aggregatedResults) {
            csvBuilder.append(result.algorithm()).append(",")
                    .append(result.problem()).append(",")
                    .append(result.iteration()).append(",")
                    .append(result.indicator()).append(",")
                    .append(result.average()).append(",")
                    .append(result.median()).append(",")
                    .append(result.standardDeviation()).append(",")
                    .append(result.count()).append("\n");
        }

        return csvBuilder.toString();
    }

    public byte[] generateChartContent(
            String startDate,
            String endDate,
            List<String> algorithms,
            List<String> problems,
            List<String> metrics,
            String groupName
    ) throws IOException {
        List<AggregatedExperimentDto> aggregatedResults = getAggregatedResults(
                startDate, endDate, algorithms, problems, metrics, groupName
        );

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, List<AggregatedExperimentDto>> groupedResults = aggregatedResults.stream()
                .collect(Collectors.groupingBy(result -> result.problem() + "-" + result.indicator()));

        for (Map.Entry<String, List<AggregatedExperimentDto>> entry : groupedResults.entrySet()) {
            String[] keys = entry.getKey().split("-");
            String problem = keys[0];
            String indicator = keys[1];
            String seriesTitle = problem + " - " + indicator;

            for (AggregatedExperimentDto result : entry.getValue()) {
                dataset.addValue((Number) result.average(), seriesTitle, result.iteration());
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Aggregated Results",
                "Iteration",
                "Average",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
        return outputStream.toByteArray();
    }
}