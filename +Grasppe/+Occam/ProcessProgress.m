classdef ProcessProgress < Grasppe.Core.Prototype
  %PROCESSPROGRESS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Tasks       = Grasppe.Occam.ProcessTask.empty();
    Maximum     = [];
    Window      = [];
    ActiveTask  = [];
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
      
      if isscalar(obj.Maximum) && isnumeric(obj.Maximum)
        progress = progress * obj.Maximum/100;
      end
    end
    
    function addProgressListener(obj, listener)
      
      callback = @(source, data) listener.progressUpdate(source, data);
      
      progressListeners = obj.progressListeners;
      
      if ~any(cellfun(@(x)isequal(x, listener), progressListeners))
        hListener  = addlistener(obj, 'ProgressChange', callback);
        progressListeners{end+1}  = listener;
        
        obj.progressListeners     = progressListeners;
      end
    end
    
    function activateTask(obj, task)
      obj.ActiveTask = task;
    end
    
    function resetTasks(obj)
      
      tasks     = obj.Tasks;
      ntasks    = numel(tasks);      
      
      for m = 1:ntasks
        try delete(tasks(m)); end
      end
      
      obj.Tasks = Grasppe.Occam.ProcessTask.empty();
      obj.updateProgress;
    end
    
    function task = addTask(obj, title, load, varargin)
      %obj.UpdateProgressComponents;
      task = Grasppe.Occam.ProcessTask(obj, title, load, varargin{:});
      
      obj.Tasks(end+1) = task;
    end
  end
  
  methods (Hidden)
    function updateProgress(obj)
      tasks     = obj.Tasks;
      ntasks    = numel(tasks);
      active    = obj.ActiveTask;
      
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
      
      overall = obj.OverallProgress*100;
      
      if progressChange
        
        %dsc = 'Running';
        
        %cnt = sprintf('%0.0f', ;
        
        if load==0
          s = '';
          overall = [];
        elseif round(progress)==round(load)
          s = '';
          overall = [];
        else          
          s = sprintf('Processing: %d of %d', round([progress, load])); %overall*100
          
          try
            s = sprintf('%s: %d of %d', active.Title, round([active.Progress, active.Load])); %overall*100
            % disp(s);
%           catch err
%             %disp(err);
%             x=1;
          end
        end
        
        
        
        h=obj.Window;
        if ~isscalar(h) || ~ishandle(h)
          h = 0;
        end
          
        try
          status(s, h, overall);
          %status('', 0, []);
        catch err
          status(s, 0);
        end
          %dispf('Progress: %0.0f (%0.0f / %0.0f)', obj.OverallProgress*100, progress, load);
      end
    end
  end
  
end

