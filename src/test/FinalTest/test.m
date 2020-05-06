% PROGRAM START
           entry
% processing t1 stores 1
           addi R1, R0, 1
           sw lit_t1(R0), R1
% processing x = 1
           lw R1, lit_t1(R0)
           sw var_x(R0), R1
% writing to console
           lw R1, var_x(R0)
           jl R15, putint
% processing t2 stores 5
           addi R1, R0, 5
           sw lit_t2(R0), R1
% processing x add/sub/or 5
           lw R2, var_x(R0)
           lw R3, lit_t2(R0)
           add R1, R2, R3
           sw temp_t3(R0), R1
% writing to console
           lw R1, temp_t3(R0)
           jl R15, putint
           hlt
% PROGRAM END

% space for var_x
var_x res 4 
% space for constant 1
lit_t1 res 4 
% space for constant 5
lit_t2 res 4 
% space for temp_t3
temp_t3 res 4
