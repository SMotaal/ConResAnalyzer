classdef PatchSeriesProcessor < Grasppe.ConRes.PatchGenerator.PatchGeneratorProcessor
  %PATCHSERIESPROCESSOR Run patch generator series
  %   Detailed explanation goes here
  
  properties
    % PatchProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Patch;
    % ScreenProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Screen;
    % PrintProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Print;
    % ScanProcessor     = Grasppe.ConRes.PatchGenerator.Processors.Scan;
    % UserProcessor     = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;
    % View
    
    Codes
  end
  
  methods
    function output = Run(obj)
      
      import(eval(NS.CLASS));
      %debugging = true;
      
      obj.ResetProcess();
      obj.prepareTasks('Patch Generation', 4);
      
      drawnow expose update;
      
      try
        obj.PrepareParameters();
        
        %% Prepare Models
        output            = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
        reference         = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
        patch             = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
        screen            = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
        
        %% Prepare Processors
        
        patchProcessor    = obj.PatchProcessor;
        screenProcessor   = obj.ScreenProcessor;
        scanProcessor     = obj.ScanProcessor;
        
        parameters        = obj.Parameters;
        
        %% Determine Variable Fields
        
        fieldTable  = cell(0,3);
        fieldVars   = [];
        
        parameterGroups = fieldnames(parameters);
        for m = 1:numel(parameterGroups)-1
          groupName       = parameterGroups{m};
          groupParameters = parameters.(groupName);
          try
            groupFields     = fieldnames(groupParameters);
            for n = 1:numel(groupFields)
              try
                fieldName                   = groupFields{n};
                fieldValue                  = parameters.(groupName).(fieldName);
                fieldTable(end+1, 1:3)  = {groupName, fieldName, fieldValue};
                if ~ischar(fieldValue) && numel(fieldValue)>1
                  fieldVars(end+1)       = numel(fieldValue);
                else
                  fieldVars(end+1)       = 1;
                end
              catch err
                debugStamp(err, 2);
              end
            end
          catch err
            debugStamp(err, 2);
          end
        end
        
        fieldCount  = numel(fieldVars);
      catch err
        debugStamp(err, 1);
      end
      
      % PREP 1/4 ***************************************************************
      CHECK(obj.Tasks.Prep);
      
      try
        
        %% Generate Variable Grid
        
        fieldRange  = cell(fieldCount,1);
        
        varRange    = '';
        for m = 1:fieldCount
          fieldRange{m} = 1:fieldVars(m);
          varRange  = [varRange 'varGrid{' int2str(m) '} '];
        end
        
        eval(['[' varRange '] = ndgrid(fieldRange{:});']);
        
        
        varCount      = numel(varGrid{1});
        
        %% Compile Patch-Variable Index Grid
        patchGrid  = zeros(varCount, fieldCount);
        
        for m = 1:varCount
          for n = 1:fieldCount
            patchGrid(m,n) = varGrid{n}(m);
          end
        end
        
        % Determine Valid Halftone Patch Grid
        meanRange               = fieldTable{1,3}(patchGrid(:,1));
        conRange                = fieldTable{2,3}(patchGrid(:,2));
        resRange                = fieldTable{3,3}(patchGrid(:,3));
        halftoneGrid            = patchGrid((meanRange+conRange./2)<=100 & (meanRange-conRange./2)>=0, :);
        halftoneRows            = size(halftoneGrid,1);
        
        %patchGridIdx            = @(g) eval('[a b c]=intersect(g, halftoneGrid, ''rows'', ''stable''); c');
        
        % Determine Screening Patch Index
        screenGrid              = halftoneGrid;
        screenGrid(:,2:3)       = 1; % Output and reuse on first use
        screenGrid              = unique(screenGrid, 'rows', 'stable');
        %screenIdx               = patchGridIdx(screenGrid);
        [s1, i1, screenIdx]     = intersect(  screenGrid,   halftoneGrid, 'rows', 'stable');
        screenRows              = size(screenGrid,1);
        
        % Determine Contone Patch Index
        contoneGrid             = halftoneGrid;
        contoneGrid(:,4:end)    = 1; % Output and reuse on first use
        contoneGrid             = unique(contoneGrid, 'rows', 'stable');
        %contoneIdx              = patchGridIdx(contoneGrid);
        [s2, i2, contoneIdx]    = intersect(  contoneGrid,  halftoneGrid, 'rows', 'stable');
        contoneRows             = size(contoneGrid,1);
        
        % Determine Monotone Patch Index
        monotoneGrid            = contoneGrid;
        monotoneGrid(:, 1)      = 1; % Output and reuse on first use
        monotoneGrid(:, 2)      = 1; % Output and reuse on first use
        monotoneGrid(:, 4:end)  = 1; % Output and reuse on first use
        monotoneGrid            = unique(monotoneGrid, 'rows', 'stable');
        %monotoneIdx             = patchGridIdx(monotoneGrid);
        [s3, i3, monotoneIdx]   = intersect(  monotoneGrid, halftoneGrid, 'rows', 'stable');
        monotoneRows            = size(monotoneGrid,1);
        
        % Validate Series Indices
        validateIdx             = @(r, g) all(r<=halftoneRows) & all(all(halftoneGrid(r, :)-g==0));
        screenValid             = validateIdx(screenIdx,    screenGrid  );
        contoneValid            = validateIdx(contoneIdx,   contoneGrid );
        monotoneValid           = validateIdx(monotoneIdx,  monotoneGrid);
        seriesValid             = screenValid & contoneValid & monotoneValid;
        
      catch err
        debugStamp(err, 1);
      end
      
      % PREP 2/4 ***************************************************************
      CHECK(obj.Tasks.Prep);
      
      try
        %% Prepare Processors
        
      catch err
        debugStamp(err, 1);
      end
      
      % PREP 3/4 ***************************************************************
      CHECK(obj.Tasks.Prep);
      
      %% Variable Tasks Load Estimation
      nPatches    = [halftoneRows];
      nExtras     = [halftoneRows, screenRows, contoneRows, monotoneRows];
      nImages     = 2;
      nStatistics = 1;
      nSteps      = [nImages nStatistics]; % [Image+Retina Statistics
      n         = numel(fieldnames(obj.Parameters.Processors));
      n         = n + 2*(n-5) + 2;
      
      SEAL(obj.Tasks.Prep); % 3
      
      % obj.updateTasks('Generating Patch', n);
      
      parameters = copy(parameters);
      
      seriesStr = {};
      
      for m = 1:halftoneRows
        
        valueIndex = halftoneGrid(m, :);
        
        screenOutput    = any(m==screenIdx);
        contoneOutput   = any(m==contoneIdx);
        monotoneOutput  = any(m==monotoneIdx);
        
        for n = 1:size(fieldTable, 1)
          v = valueIndex(n);
          if v>0, v = fieldTable{n,3}(v); end
          parameters.(fieldTable{n,1}).(fieldTable{n,2}) = v;
          
          seriesStr{end+1,1} = obj.ParameterID(parameters);
          
          if screenOutput
            seriesStr{end+1,1} = obj.ParameterID(parameters, 'screen');
          end
          
          if contoneOutput
            seriesStr{end+1,1} = obj.ParameterID(parameters, 'contone');
          end
          
          if monotoneOutput
            seriesStr{end+1,1} = obj.ParameterID(parameters, 'monotone');
          end
          
        end
        
        
      end
      
      output = seriesStr; %[]; %{output{:}, strvcat(seriesStr)};
      
    end
    
    function str = ParameterID(obj, parameters, type)
      
      import Grasppe.ConRes.PatchGenerator.Processors.*;
      
      %% Patch Abbreviations
      
      if ~exist('type', 'var'), type = 'normal'; end
      
      switch lower(type)
        case {'screen', 'scr', 's'}
          codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
          codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
          % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
          codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
          codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
          codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
          codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
          codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
          codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
          codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
          codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
        case {'contone', 'cont', 'con', 'c'}
          codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
          codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
          codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
          codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
          codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
        case {'monotone', 'mono', 'm'}
          codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
          codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
          codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
        otherwise
          codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
          codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
          codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
          codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
          
          %% Screen codes
          % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
          codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
          codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
          codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
          
          %% Print codes
          codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
          codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
          codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
          codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
          
          %% Scan codes
          codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
          codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
      end
      
      obj.Codes                       = codes;
      
      
      parameterGroups = fieldnames(parameters);
      
      str = '';
      for m = 1:numel(parameterGroups)
        groupName           = parameterGroups{m};
        groupParameters     = parameters.(groupName);
        groupString         = [groupName '{'];
        try
          groupFields       = fieldnames(groupParameters);
          for n = 1:numel(groupFields)
            try
              fieldName     = groupFields{n};
              fieldValue    = parameters.(groupName).(fieldName);
              
              fieldCode     = codes.(groupName).(fieldName);
              fieldLabel    = fieldCode{1};
              fieldSize     = [fieldCode{2:3}];
              fieldFactor   = fieldCode{4};
              fieldDefault  = fieldCode{5};
              
              if isequal(fieldDefault, fieldValue), continue; end
              
              if isnumeric(fieldValue)
                if isscalar(fieldFactor), fieldValue  = fieldValue.*fieldFactor; end
                fieldString   = strrep(num2str(fieldValue, ['%0' int2str(fieldSize(1)) '.0f' ]), '.', '');  %'%03.2f'),
              elseif ~ischar(fieldValue)
                fieldString   = toString(fieldValue);
              end
              
              valueLength     = numel(fieldString);
              
              if isnumeric(fieldSize)
                fieldString     = fieldString(1:min(fieldSize(2), max(fieldSize(1), valueLength)));
              end
              
              str             = [str '-' fieldLabel fieldString]; %'-'
              % end
            end
          end
        end
      end
      
      str = str(2:end);
      
    end
    
    function str = ParameterString(obj, parameters)
      
      parameterGroups = fieldnames(parameters);
      
      str = cell(numel(parameterGroups),1);
      for m = 1:numel(parameterGroups)
        groupName         = parameterGroups{m};
        groupParameters   = parameters.(groupName);
        groupString       = [groupName '{'];
        try
          groupFields     = fieldnames(groupParameters);
          for n = 1:numel(groupFields)
            try
              fieldName   = groupFields{n};
              fieldValue  = parameters.(groupName).(fieldName);
              groupString = [groupString fieldName ': ' toString(fieldValue) ';\t'];
            end
          end
        end
        groupString       = sprintf([strtrim(groupString) '}\t']);
        str{m,1}          = groupString;
      end
      
      str = strcat(str{:}, '\t');
    end
    
    function ExecuteFunction(obj)
      ConResLab.declareFunctions;
    end
    
    function PrepareParameters(obj)
      import(eval(NS.CLASS));
      
      import Grasppe.ConRes.PatchGenerator.Processors.*;
      
      parameters = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
      
      parameters.Patch                    = struct();
      parameters.Screen                   = struct();
      parameters.Scan                     = struct();
      
      %% Patch Parameters
      parameters.Patch.(Patch.MEANTONE)   = [25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
      parameters.Patch.(Patch.CONTRAST)   = [33 66 50]; Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
      parameters.Patch.(Patch.RESOLUTION) = Grasppe.Kit.ConRes.ResolutionRange; %4.386;
      parameters.Patch.(Patch.SIZE)       = 5.3;
      
      %% Screen Parameters
      parameters.Screen.(Screen.PPI)      = obj.PatchProcessor.Addressability;
      parameters.Screen.(Screen.SPI)      = 2450;
      parameters.Screen.(Screen.LPI)      = [150 175];
      parameters.Screen.(Screen.ANGLE)    = 37.5; %0:7.5:37.5;
      
      %% Print Parameters
      parameters.Screen.(Screen.TVI)      = 0;
      parameters.Screen.(Screen.NOISE)    = 0;
      parameters.Screen.(Screen.RADIUS)   = 0;
      parameters.Screen.(Screen.BLUR)     = 0;
      
      %% Scan Parameters
      parameters.Scan.(Scan.DPI)          = 1200;
      parameters.Scan.(Scan.SCALE)        = 100;
      
      obj.Parameters                      = parameters;
      
    end
    
    
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
  
end

