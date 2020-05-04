% processing x + x
           lw R2, param_x(R0)
           lw R3, param_x(R0)
           add R1, R2, R3
           sw add_t1(R0), R1
% processing result = +
           lw R1, add_t1(R0)
           sw var_result(R0), R1
% PROGRAM START
           entry
% processing t2 stores 1
           addi R3, R0, 1
           sw lit_1(R0), R3
% processing t3 stores 1
           addi R3, R0, 1
           sw lit_1(R0), R3
% processing x = x
           lw R3, var_x(R0)
           sw var_x(R0), R3
% processing t4 stores 1
           addi R3, R0, 1
           sw lit_1(R0), R3
% processing t5 stores 1
           addi R3, R0, 1
           sw lit_1(R0), R3
           hlt
% PROGRAM END

% space for var_result
var_result res 4 
% space for temp_t1
add_t1 res 4
% space for var_x
var_x res 4 
% space for constant 1
lit_1 res 4 
% space for constant 1
lit_1 res 4 
% space for constant 1
lit_1 res 4 
% space for constant 1
lit_1 res 4 
