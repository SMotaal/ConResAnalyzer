function parameters = PrepareParameters(obj)
  import(eval(NS.CLASS));
  
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  
  parameters = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
  
  parameters.Patch                    = struct();
  parameters.Screen                   = struct();
  parameters.Scan                     = struct();
  
  %% Patch Parameters
  parameters.Patch.(Patch.MEANTONE)   = 5:15:95;        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
  parameters.Patch.(Patch.CONTRAST)   = [33 66 50];     % Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
  parameters.Patch.(Patch.RESOLUTION) = [2.155 4.386];  % Grasppe.Kit.ConRes.ResolutionRange; %4.386;
  parameters.Patch.(Patch.SIZE)       = 5.3;
  
  %% Screen Parameters
  parameters.Screen.(Screen.PPI)      = obj.PatchProcessor.Addressability;
  parameters.Screen.(Screen.SPI)      = 2450;
  parameters.Screen.(Screen.LPI)      = [100 175];
  parameters.Screen.(Screen.ANGLE)    = 37.5; %0:7.5:37.5;
  
  %% Print Parameters
  parameters.Screen.(Screen.TVI)      = 0;
  parameters.Screen.(Screen.NOISE)    = 0;
  parameters.Screen.(Screen.RADIUS)   = 0;
  parameters.Screen.(Screen.BLUR)     = 0;
  
  %% Scan Parameters
  parameters.Scan.(Scan.DPI)          = 1270;
  parameters.Scan.(Scan.SCALE)        = 100;
  
  if narout==0
    obj.Parameters                      = parameters;
  end
  
end
