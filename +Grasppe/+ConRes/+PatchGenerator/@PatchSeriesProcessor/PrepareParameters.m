function parameters = PrepareParameters(obj)
  import(eval(NS.CLASS));
  
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  
  global testmode;
    
  testing                               = isequal(testmode, true); % true;
  
  parameters                            = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
  
  parameters.Patch                      = struct();
  parameters.Screen                     = struct();
  parameters.Scan                       = struct();
  
  %% Patch Parameters
  if ~testing
    parameters.Patch.(Patch.MEANTONE)   = [25 50 75]; %5:15:95;        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
    parameters.Patch.(Patch.CONTRAST)   = [100	59.95	35.94	21.54	12.92	7.74	4.64	2.78	1.67	1]; %Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
    parameters.Patch.(Patch.RESOLUTION) = [0.63	0.81	1.04	1.35	1.74	2.24	2.91	3.76	4.85	6.25];%Grasppe.Kit.ConRes.ResolutionRange; %[2.155 4.386];
  else
    parameters.Patch.(Patch.MEANTONE)   = [25 50 75];        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
    parameters.Patch.(Patch.CONTRAST)   = [100	59.95 12.92 2.78 1];     % Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
    parameters.Patch.(Patch.RESOLUTION) = [0.63 3.76	6.25];  % Grasppe.Kit.ConRes.ResolutionRange; %[2.155 4.386];
  end
  
  parameters.Patch.(Patch.SIZE)         = 5.3;
  
  %% Screen Parameters
  parameters.Screen.(Screen.PPI)        = 3600; %obj.PatchProcessor.Addressability;
  parameters.Screen.(Screen.SPI)        = 2540; ... %2540; 
    parameters.Screen.(Screen.PPI)      = parameters.Screen.(Screen.SPI); %2540; %3600; %obj.PatchProcessor.Addressability;
  parameters.Screen.(Screen.LPI)        = [175];  %100];
  parameters.Screen.(Screen.ANGLE)      = 37.5; %0:7.5:37.5; %0; %
  
  %% Print Parameters
  parameters.Screen.(Screen.TVI)        = 0;
  parameters.Screen.(Screen.NOISE)      = 0;
  parameters.Screen.(Screen.RADIUS)     = 0;
  parameters.Screen.(Screen.BLUR)       = 0;
  
  %% Scan Parameters
  parameters.Scan.(Scan.DPI)            = 2400; %600; %1270;
  parameters.Scan.(Scan.SCALE)          = 100;
  
  if nargout==0
    obj.Parameters                      = parameters;
  end
  
end
