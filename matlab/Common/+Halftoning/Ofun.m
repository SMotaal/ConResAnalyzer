
% OFUN Computes Objective Function for Vector Error Diffusion
%      [J,G] = OFUN(X,C,d) 
% Given the Covariance Matrix C, Vectors X and d
% returns the minimum value J of ||Cx -d||
% and the the direction for descent G

% Part of Halftoning Toolbox Version 1.2 released July 2005

% Copyright (c) 1999-2005 The University of Texas
% All Rights Reserved.

function [J,G] = Ofun(X,C,d)

J = (C*X-d)'*(C*X-d);
G = 2*(C*X-C'*d);

