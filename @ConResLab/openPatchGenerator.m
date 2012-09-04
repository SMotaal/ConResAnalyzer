function openPatchGenerator()
try
  evalin('base', 'grasppe;');
catch err
  addpath('Grasppe.MatLab') %, 'Common', 'UniformPrinting', 'DigitalImageProcessing');
  evalin('base', 'grasppe;');
end

addpath('Common');

close all; cleardebug; clc;

global debugmode; debugmode=false;
javaaddpath(fullfile(cd, 'Classes'));

GrasppeKit.DelayedCall(@(s, e)launch,[],'start');

end

function launch()
  
patchGenerator = Grasppe.ConRes.PatchGenerator.PatchGenerator;

end
