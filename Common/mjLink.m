function [ instance ] = mjLink( input_args )
  %MJLINK Grasppe MatLab-Java Link
  %   Returns the MJLink singlton instance which provides low-level
  %   functionality to connect MatLab and Java components.
  
  Grasppe.Lure.Framework.MJLink.InitializeJava;
  
  
%   import Grasppe.Lure.Framework.*;
%   
%   global MJLinkInstance;
%   
%   try
%     MJLinkInstance = evalin('base', 'MJLinkInstance');
%   catch err
%     MJLinkInstance = [];
%   end
%   
%   
%   if isempty(MJLinkInstance)
%      MJLinkInstance = MJLink.GetInstance;
%      assignin('base', 'mjLink', MJLinkInstance);
%   end
%   
%   instance = MJLinkInstance;
%   
%   pause(1);
%   
%   assignin('caller', 'mjLink', MJLinkInstance);
end

