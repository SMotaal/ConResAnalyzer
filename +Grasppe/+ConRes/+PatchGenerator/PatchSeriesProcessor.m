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
  end
  
  methods
    function output = Run(obj)
      
      import(eval(NS.CLASS));
      
      % import Grasppe.ConRes.PatchGenerator.Processors.Print;
      % import Grasppe.ConRes.PatchGenerator.Processors.Screen;
      % import Grasppe.ConRes.PatchGenerator.Processors.Scan;
      
      %debugging = true;
      
      obj.ResetProcess();
      obj.prepareTasks('Patch Generation');
      
      if isa(obj.View, 'Grasppe.Occam.Process')
        panel = obj.View;
      else
        panel = [];
      end
      
      if ~isempty(panel), panel.clearAxes(); end
      
      drawnow expose update;
      
      CHECK(obj.Tasks.Prep); % 1
      
      try
        obj.PrepareParameters();
        CHECK(obj.Tasks.Prep); % 2
      catch err
        debugStamp(err, 1);
      end
      
      %% Variable Tasks Load Estimation
      % n         = numel(fieldnames(obj.Parameters.Processors));
      % n         = n + 2*(n-5) + 2;
      
      SEAL(obj.Tasks.Prep); % 3
      % obj.updateTasks('Generating Patch', n);
      
    end
    
    function PrepareParameters(obj)
      import(eval(NS.CLASS));
      
      import Grasppe.ConRes.PatchGenerator.Processors.*;
      
      parameters = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
      
      parameters.Patch                    = struct();
      parameters.Screen                   = struct();
      parameters.Scan                     = struct();
      
      %% Patch Parameters
      parameters.Patch.(Patch.MEANTONE)   = 50;
      parameters.Patch.(Patch.CONTRAST)   = 50;
      parameters.Patch.(Patch.RESOLUTION) = 4.386;
      parameters.Patch.(Patch.SIZE)       = 5.3;
      
      %% Screen Parameters
      parameters.Screen.(Screen.PPI)      = obj.PatchProcessor.Addressability;
      parameters.Screen.(Screen.SPI)      = 2450;
      parameters.Screen.(Screen.LPI)      = 175;
      parameters.Screen.(Screen.ANGLE)    = 37.5;
      
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
  end
  
end

