try
  grasppe;
catch err
  addpath('Grasppe.MatLab') %, 'Common', 'UniformPrinting', 'DigitalImageProcessing');
  grasppe;
end
addpath('Common');
close all; cleardebug; clc;
global debugmode; debugmode=false;
javaaddpath(fullfile(cd, 'Classes'));
patchGenerator = Grasppe.ConRes.PatchGenerator.PatchGenerator;
