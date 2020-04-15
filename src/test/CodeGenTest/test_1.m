% PROGRAM START
           entry
% processing t1 stores 5
           addi R1, R0, 5
           sw lit_t1(R0), R1
% processing a = 5
           lw R1, lit_t1(R0)
           sw var_a(R0), R1
% writing to console
           lw R1, var_a(R0)
           jl R15, putint
           hlt
% PROGRAM END

% space for var_a
var_a res 4 
% space for constant 5
lit_t1 res 4 
