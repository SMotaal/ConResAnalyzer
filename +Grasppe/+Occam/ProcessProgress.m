classdef ProcessProgress < Grasppe.Core.Prototype
  %PROCESSPROGRESS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Tasks       = Grasppe.Occam.ProcessTask.empty();
  end
  
  properties (Dependent)
    OverallProgress
  end
  
  properties (Hidden)
    load              = [];
    progress          = [];
    progressListeners = {};
  end
  
  events
    ProgressChange
  end
  
  methods
    function progress = get.OverallProgress(obj)
      load      = obj.load;
      progress  = obj.progress;
      
      if isempty(load) || isempty(progress) || load==0 % || progress==0
        progress = [];
        return;
      end
      
      % if isempty(progress)
      %   progress = 0;
      %   return;
      % end
      
      progress  = progress / load;
    end
    
    function addProgressListener(obj, listener)
      
      callback = @(source, data) listener.progressUpdate(source, data);
      
      progressListeners = obj.progressListeners;
      
      if ~any(cellfun(@(x)isequal(x, listener), progressListeners))
        hListener  = addlistener(obj, 'ProgressChanged', callback);
        progressListeners{end+1}  = listener;
        
        obj.progressListeners     = progressListeners;
      end
    end
    
    function task = addTask(obj, title, load, varargin)
      %obj.UpdateProgressComponents;
      task = Grasppe.Occam.ProcessTask(obj, title, load, varargin{:});
    end
  end
  
  methods (Hidden)
    function updateProgress(obj)
      tasks     = obj.Tasks;
      ntasks    = numel(tasks);
      
      load      = 0;
      progress  = 0;
      
      for m = 1:ntasks
        task      = tasks(m);
        factor    = abs(task.Factor);
        load      = load      + abs(task.Load     * factor);
        progress  = progress  + abs(task.Progress * factor);
      end
      
      progressChange  = ~isequal(obj.load, load) || ~isequal(obj.progress, progress);
      
      obj.load      = load;
      obj.progress  = progress;
      
      if progressChange
          dispf('Progress: %0.0f (%0.0f / %0.0f)', obj.OverallProgress, progress, load);
      end
    end
  end
  
end

