function [ output_args ] = initialize( input_args )
  %INITIALIZE Summary of this function goes here
  
  addpath('Common');
  
  % GrasppePrototype.InitializeGrasppePrototypes;
  
  timerID     = 'InitializeTimer';
  delayTimer  = timerfind('Tag','StartupTimer');
  
  if isempty(delayTimer)
    fprintf(1,'\n\nHello!\n');
    delayTimer = timer('Tag','StartupTimer', 'StartDelay', 0.5, ...
      'TimerFcn', 'initialize;');
    start(delayTimer);
    fprintf(2,'\nWorkspace: '); fprintf(1, 'Loading...  \n');
  else
    try
      stop(delayTimer);
      delete(delayTimer);
    end
    initializeScript();
  end
end

function initializeScript()
  % PersistentSources readonly;
  % PersistentSources load; %PersistentSources load;
  fprintf(2,'\nWorkspace: '); fprintf(1, 'Ready\n');
  setstatus(0,'');
  
end

