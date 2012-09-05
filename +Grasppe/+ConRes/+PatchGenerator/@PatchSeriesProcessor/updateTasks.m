function updateTasks(obj, activeTask, taskTitle, numRender, numAnalyze, numExport, numCompile)
  tasks   = fieldnames(obj.Tasks);  %{'Render', 'Analyze', 'Export', 'Compile'};
  
  if exist('activeTask') && ~isempty(activeTask) ischar(activeTask)
    try taskIdx = strcmpi(activeTask, tasks); end
    if any(taskIdx)
      try obj.ProcessProgress.activateTask(obj.Tasks.(tasks{taskIdx})); end
      
      if ~obj.Tasks.Prep.isCompleted(),
        SEAL(obj.Tasks.Prep);
      end
    end
  end
  
  if exist('taskTitle', 'var') && ~isempty(taskTitle) && ischar(taskTitle)
    try obj.ProcessProgress.ActiveTask.Title = taskTitle; end
  end
  
  for m = 1:numel(tasks)
    task = tasks{m};
    
    numTask = [];               % if exist(['num' task], 'var')
    try numTask = eval(['num' task]); end
    
    if isscalar(numTask) && isnumeric(numTask)
      obj.Tasks.(task).Load  = numTask;
    end
  end
end

end
