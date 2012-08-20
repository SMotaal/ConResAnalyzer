addpath('Common', 'UniformPrinting', 'DigitalImageProcessing');
close all; cleardebug; clc;
global debugmode; debugmode=false;
javaaddpath(fullfile(cd, 'Classes'));
patchGenerator = Grasppe.ConRes.PatchGenerator.PatchGenerator;
