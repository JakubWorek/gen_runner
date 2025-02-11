# GENRunner

An application for managing experiments with multi-objective evolutionary algorithms (MOEA) based on the [MOEA Framework](http://moeaframework.org/). The system operates in a client-server architecture, where:

- **Server** – asynchronously runs experiments, collects results, and manages statistics.
- **Client** – a console application that allows launching experiments, viewing statuses and results, and managing experiments.

## Features

- **Running experiments:**
  - Utilizes the MOEA Framework.
  - Experiment parameters: algorithm names, test problems, metrics, and budget (number of objective function calls or iterations).
  - Configuring multiple algorithms and problems within a single experiment.
  - Experiment repetitions (running an experiment in multiple instances).

- **Asynchronous experiment execution:**
  - Running multiple experiments simultaneously, considering machine constraints.

- **Experiment management:**
  - Listing ongoing and completed experiments.
  - Checking experiment status (running, completed, error).
  - Viewing results – tabular presentation (iterations as rows, metrics as columns).
  - Aggregating statistics (mean, median, standard deviation) with date range filtering.
  - Filtering experiment instances by algorithm, problem, or metrics.

- **Results presentation:**
  - Exporting results to a CSV file.
  - Generating graphs (PNG) – metric value charts over iterations.
  - New presentation formats compatible with existing mechanisms (e.g., time-based filtering).

- **Experiment group management:**
  - Creating experiment groups by tagging (by ID or filtered results) and adding to existing groups.
  - Displaying results for experiments within a specific group.

- **API handling:**
  - The CLI client communicates with the server, handles errors, and transmits experiment parameters.

- **Experiment deletion:**
  - Deleting individual experiments or entire groups (including results and metadata).

## Team:
- Jakub Worek
- Jakub Białecki
- Bartłomiej Treśka

# How to Run:
- Start MongoDB in Docker (instructions in the `database` folder).
- Run `backend/src/main/java/pl/edu/agh/GenRunnerApplication.java`.
- Run `cli/src/main/java/cli/Client.java`.
