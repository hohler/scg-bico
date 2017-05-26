class JobControl {
	
	constructor(path) {
		this._path = path;
		this._currentJobCount = 0;
	}
	
	getCurrentJobCount() {
		return this._currentJobCount;
	}
	
	getJobStatus(jobName) {
		var that = this;
		$.get(this._path + encodeURI(jobName) + '.json', function(data) {
			if(data.errors) {
				that.updateJobStatus("ERROR");
			} else
			if(data.job) {
				if(data.job.jobInstances && Object.keys(data.job.jobInstances).length > 0) {
					var max = Math.max.apply(null, Object.keys(data.job.jobInstances).map(function(k) { return parseInt(k); }));
					that._currentJobCount = max;
					that.updateJobStatus(data.job.jobInstances[max].lastJobExecutionStatus);
					that.updateJobDuration(data.job.jobInstances[max].lastJobExecution);
				} else {
					that.updateJobStatus("NO_DATA");
				}
			} else {
				that.updateJobStatus("NO_DATA");
			}
		});
	}

	updateJobDuration(url) {
		$.get(encodeURI(url), function(data) {
			if(data.jobExecution && data.jobExecution.duration) {
				$("#jobDuration").html("Duaration: "+data.jobExecution.duration);	
			} else {
				$("#jobDuration").html("");
			}
		});
	}

	updateJobStatus(status) {
		if(status == "ERROR") {
			$("#jobStatus").removeClass().addClass("label label-danger").html("Error");
			$("#jobStatus").addClass("hidden");
		} else
		if(status == "NO_DATA") {
			$("#jobStatus").removeClass().addClass("label label-default").html("No running job");
			$("#jobStart").removeClass("hidden");
		} else
		if(status == "COMPLETED") {
			if($("#jobStatus").html() == "Job running...") {
				location.reload();
			} else {
				$("#jobStatus").removeClass().addClass("label label-success").html("Job finished");
				$("#jobStart").removeClass("hidden");
			}
		} else
		if(status == "FAILED") {
			$("#jobStatus").removeClass().addClass("label label-danger").html("Job failed");
			$("#jobStart").removeClass("hidden");
		} else
		if(status == "STARTED" || status == "STARTING") {
			$("#jobStatus").removeClass().addClass("label label-info").html("Job running...");
			$("#jobStart").addClass("hidden");
		}
	}

	startJob(jobName) {
		$.post(this._path + encodeURI(jobName) + '.json', {'jobParameters': 'run.id(long)='+this._currentJobCount}, function(data) {
			if(data.jobExecution) {
				if(data.jobExecution.status && (data.jobExecution.status == "STARTING" || data.jobExecution.status == "STARTED")) {
					$("#jobStatus").removeClass().addClass("label label-info").html("Job starting...");
					$("#jobStart").addClass("hidden");
				} else {
					$("#jobStatus").removeClass().addClass("label label-danger").html("Failed job start");
					$("#jobSTart").removeClass("hidden");
				}
			} else {
				$("#jobStatus").removeClass().addClass("label label-danger").html("Failed job start");
				$("#jobSTart").removeClass("hidden");
			}
		});
	}
}