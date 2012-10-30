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
    parameters.Patch.(Patch.MEANTONE)   = 5:15:95;        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
    parameters.Patch.(Patch.CONTRAST)   = Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
    parameters.Patch.(Patch.RESOLUTION) = Grasppe.Kit.ConRes.ResolutionRange; %[2.155 4.386];
    
  else
    parameters.Patch.(Patch.MEANTONE)   = 5:15:95;        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
    parameters.Patch.(Patch.CONTRAST)   = [33 66 50];     % Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
    parameters.Patch.(Patch.RESOLUTION) = [2.155 4.386];  % Grasppe.Kit.ConRes.ResolutionRange; %[2.155 4.386];
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
  parameters.Scan.(Scan.DPI)            = 1270;
  parameters.Scan.(Scan.SCALE)          = 100;
  
  if nargout==0
    obj.Parameters                      = parameters;
  end
  
end
