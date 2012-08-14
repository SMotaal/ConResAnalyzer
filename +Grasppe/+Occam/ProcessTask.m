classdef ProcessTask < Grasppe.Core.Prototype
  %PROCESSTASK Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Title     = '';
    Load      = 1;
    Progress  = 0;
    Factor    = 1;
    Process   = [];
  end
  
  events
    ProgressChange
  end
  
  methods
    function obj = ProcessTask(process, varargin)     
      properties = {'Title', 'Load', 'Factor', 'Progress'};
      
      obj.Process = process;      
      
      for p = 1:min(nargin-1, numel(properties))
        obj.(properties{p})  = varargin{p};
      end

      %if nargin >= 1, obj.Title     = varargin{1}; end
      %if nargin >= 2, obj.Load      = varargin{2}; end
      %if nargin >= 3, obj.Progress  = varargin{3}; end
    end
    
    function progressCheck(obj, varargin)
      if isempty(obj.Progress), obj.Progress = 0; end
      
      check = 1;
      load  = obj.Load;
      
      if nargin>=2, check = varargin{1}; end
      
      progress  = obj.Progress + check;
      
      %if progress > obj.Load      end
      
      progress  = min(progress, load);
      
      obj.Progress = progress;
      
      try
        obj.Process.updateProgress;
      end
      
      try 
        notify(obj.Process, 'ProgressChange');
      end
    end
    
    function CHECK(obj, varargin)
      obj.progressCheck(varargin{:});
    end
    
    function SEAL(obj)
      obj.progressCheck(obj.Load);
    end
  end
  
end

