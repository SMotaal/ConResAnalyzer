function [ instance ] = GetInstance( instance, instanceClass )
  %GETINSTANCE Summary of this function goes here
  %   Detailed explanation goes here
  
	% instanceClass = eval(CLASS);
  
  if ~exist('instance', 'var')
    instance = evalin('caller', 'Instance'); 
  end
  
  if ~exist('instanceClass', 'var')
    instanceClass = evalin('caller', CLASS);
  end
  
  valid = true; verified = true;
  
  try verified  = verified & ~isempty(instance); end
  try verified  = verified & ~isa(instance, instanceClass); end
  try valid     = isvalid(verfied); end
      
  if ~verified || ~valid
    instance = eval(instanceClass);
%   else
%     instance = Instance;
  end
  
  assignin('caller', 'Instance', instance);
  
end

