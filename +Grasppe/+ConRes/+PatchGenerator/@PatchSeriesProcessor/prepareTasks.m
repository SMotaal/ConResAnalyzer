function prepareTasks(obj, title, ...
    numPrep, numRender, numAnalyze, numExport, numCompile)
  if ~exist('title',      'var'), title       = 'Processing'; end
  if ~exist('numPrep',    'var'), numPrep     = 3; end
  if ~exist('numRender' , 'var'), numRender   = 1; end
  if ~exist('numAnalyze', 'var'), numAnalyze  = 1; end
  if ~exist('numExport' , 'var'), numExport   = 1; end
  if ~exist('numCompile', 'var'), numCompile  = 1; end
  
  obj.Tasks.Prep    = obj.ProcessProgress.addAllocatedTask(['Preparing '  title],   5, numPrep);
  obj.Tasks.Render  = obj.ProcessProgress.addAllocatedTask(['Rendering '  title],  40, numRender);
  obj.Tasks.Analyze = obj.ProcessProgress.addAllocatedTask(['Analyzing '  title],  30, numAnalyze);
  obj.Tasks.Export  = obj.ProcessProgress.addAllocatedTask(['Exporting '  title],  20, numExport);
  obj.Tasks.Compile = obj.ProcessProgress.addAllocatedTask(['Compiling '  title],   5, numCompile);
  
  
  obj.ProcessProgress.activateTask(obj.Tasks.Prep);
  
  drawnow expose update;
end
