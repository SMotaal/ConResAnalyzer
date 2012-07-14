function [ instance ] = mjLink( input_args )
  %MJLINK Grasppe MatLab-Java Link
  %   Returns the MJLink singlton instance which provides low-level
  %   functionality to connect MatLab and Java components.
  
  
  import Grasppe.Lure.Framework.*;
  
  persistent Instance;
  
  if isempty(Instance)
     Instance = MJLink.GetInstance;
  end
  
  instance = Instance;
  
  assignin('caller', 'mjLink', instance);
end

