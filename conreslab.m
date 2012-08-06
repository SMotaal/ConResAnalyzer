addpath('Common', 'UniformPrinting', 'DigitalImageProcessing');
close all; cleardebug; clc;
javaaddpath(fullfile(cd, 'Classes'));
patchGenerator = Grasppe.ConRes.PatchGenerator.PatchGenerator;
