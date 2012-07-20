classdef Process < Grasppe.Occam.ProcessData % handle & matlab.mixin.Heterogeneous
  %GENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (AbortSet)
    Input
    Output
    Status
    Results
    Processes = {};%Grasppe.Occam.Process.empty; %eval([CLASS '.empty()']);
    Window = [];
  end
  
  properties (Hidden)
    status
    permanent
    processListeners
  end
  
  properties (Hidden)
    COMPLETE_STATUS = 'Finished';
    EXECUTING_STATUS = 'Executing';
    TERMINATING_STATUS = 'Terminating';
    READY_STATUS = 'Ready';
    FAILED_STATUS = 'Failed';
    NO_ERRORS = 'No Errors';
    BYPASSED_STATUS = 'Bypassed';
  end
  
  events
    ExecutionComplete
    ExecutionFailed
    ExecutionStarted
    StatusChanged
    ParametersChanged
  end
  
  %   methods (Abstract)
  %     %output = Run(obj);
  %   end
  
  methods
    
    function obj = Process()
      obj = obj@Grasppe.Occam.ProcessData();
      obj.Type = class(obj);
      % obj.Name = eval(CLASS);
    end
    
    function set.Status(obj, value)
      obj.Status = value;
      % dispf('%s: %s', class(obj), toString(value));
      notify(obj, 'StatusChanged');
    end
    
    function output = Execute(obj, parameters)
      
      try obj.Parameters = parameters; end; %if nargin > 1, try obj.Parameters = parameters; end; end
      
      if (isempty(obj.Input))
        try
          obj.Input = evalin('caller', 'output');
        end
      end
      
      input     = obj.Input;
      input     = obj.InitializeProcess(input);
      obj.Input = input;
      
      output = obj.Run;
      
      output = obj.TerminateProcess(output);
      obj.Output = output;
      
      if nargout < 1
        try assignin('caller', 'output', output); end
        clear output;
      end
      
      obj.Input = [];
    end
    
    function output = Run(obj)
      output = obj.Output;
    end
    
    function output = InitializeProcess(obj, input)
      output      = input;
      notify(obj, 'ExecutionStarted');
      obj.Results = obj.NO_ERRORS;
      obj.Status  = obj.EXECUTING_STATUS;
    end
    
    function output = TerminateProcess(obj, output)
      output      = output;
      obj.Status  = obj.TERMINATING_STATUS;
      switch obj.Results
        case obj.NO_ERRORS
          notify(obj, 'ExecutionComplete');
          obj.Status = obj.COMPLETE_STATUS;
        otherwise
          notify(obj, 'ExecutionFailed');
          obj.Status = obj.FAILED_STATUS;
      end
    end
    
    function parameters = getParameters(obj)
      parameters = obj.Parameters;
    end
    
    function addProcess(obj, process)
      if (~isa(process, eval(CLASS)))
        processorString = 'none';
        try processorString = toString(process); end
        error('Failed to add processor: %s', processorString);
      end
      
      callback = @(source, data) obj.processUpdate(source, data);
      
      try
        processListener = addlistener(obj, 'ExecutionComplete', callback);
        processListener = addlistener(obj, 'ExecutionFailed',   callback);
        processListener = addlistener(obj, 'ExecutionStarted',  callback);
        processListener = addlistener(obj, 'StatusChanged',     callback);
        processListener = addlistener(obj, 'ParametersChanged', callback);
        
        obj.Processes{end+1} = processListener;
      catch err
        disp(err);
        null;
      end
    end
    
    function processUpdate(obj, source, data)
      %switch data.EventName
      try
        
        h = obj.Window;
        if isempty(h), h = 0; end
        
        switch data.EventName
          case 'ExecutionComplete'
            string='';
            try
              if isequal(source.permanent, true)
                return;
              end
            end
          otherwise
            string = sprintf('%s: %s@%s', class(obj), data.EventName, class(source));
        end
        
        
        status(string, h);
        
        
      end
    end
    
    function h = get.Window(obj)
      h = obj.Window;
      if isempty(h)
        h = get(0,'CurrentFigure');
      end
    end
    
  end
  
end

