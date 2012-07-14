classdef Process < handle
  %GENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (AbortSet)
    Input
    Output
    Status
    Results
    Parameters
  end
  
  properties (Hidden)
    status
  end
  
  properties (Hidden)
    COMPLETE_STATUS = 'Finished';
    EXECUTING_STATUS = 'Executing';
    TERMINATING_STATUS = 'Terminating';
    READY_STATUS = 'Ready';
    FAILED_STATUS = 'Failed';
    NO_ERRORS = 'No Errors';
  end
  
  events
    ExecutionComplete
    ExecutionFailed
    ExecutionStarted
    StatusChanged
  end
  
  methods (Abstract)
    Run(obj);
  end
  
  methods
    function set.Status(obj, value)
      obj.Status = value;
      disp(value);
      notify(obj, 'StatusChanged');
    end
    
    function Execute(obj)
      obj.InitializeProcess;
      obj.Run;
      obj.TerminateProcess;
    end
    
    function InitializeProcess(obj)
      notify(obj, 'ExecutionStarted');
      obj.Results = obj.NO_ERRORS;      
      obj.Status = obj.EXECUTING_STATUS;
    end
    
    function TerminateProcess(obj)
      obj.Status = obj.TERMINATING_STATUS;
      switch obj.Results
        case 'No Errors'
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
    
  end
  
end

