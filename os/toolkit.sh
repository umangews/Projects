#!/bin/bash
# ==============================================================
#  Multi-Utility Shell Toolkit
#  A single menu-driven script combining arithmetic, loop-based,
#  and control-structure exercises into one project.
# ==============================================================

# -------------------- ARITHMETIC MODULE --------------------

sum_product_avg() {
    read -p "Enter 4 integers separated by space: " a b c d
    sum=$((a+b+c+d))
    prod=$((a*b*c*d))
    avg=$(echo "scale=2; $sum/4" | bc)
    echo "Sum = $sum"
    echo "Product = $prod"
    echo "Average = $avg"
}

calculator() {
    read -p "Enter first number: " n1
    read -p "Enter second number: " n2
    echo "1. Add  2. Subtract  3. Multiply  4. Divide"
    read -p "Choose operation: " op
    case $op in
        1) echo "Result: $((n1+n2))" ;;
        2) echo "Result: $((n1-n2))" ;;
        3) echo "Result: $((n1*n2))" ;;
        4)
           if [ "$n2" -eq 0 ]; then
               echo "Error: division by zero"
           else
               echo "Result: $(echo "scale=2; $n1/$n2" | bc)"
           fi
           ;;
        *) echo "Invalid choice" ;;
    esac
}

gross_salary() {
    read -p "Enter basic salary: " basic
    hra=$(echo "$basic*0.20" | bc)
    da=$(echo "$basic*0.30" | bc)
    gross=$(echo "$basic+$hra+$da" | bc)
    echo "HRA = $hra"
    echo "DA  = $da"
    echo "Gross Salary = $gross"
}

factorial() {
    read -p "Enter a number: " n
    fact=1
    for ((i=1; i<=n; i++)); do
        fact=$((fact*i))
    done
    echo "Factorial of $n = $fact"
}

max_min_args() {
    read -p "Enter numbers separated by space: " -a arr
    max=${arr[0]}
    min=${arr[0]}
    for num in "${arr[@]}"; do
        if [ "$num" -gt "$max" ]; then max=$num; fi
        if [ "$num" -lt "$min" ]; then min=$num; fi
    done
    echo "Max = $max"
    echo "Min = $min"
}

# -------------------- LOOP MODULE --------------------

series_sum() {
    read -p "Enter value of x: " x
    sum=1
    term=1
    for ((i=1; i<=4; i++)); do
        term=$(echo "$term*$x" | bc)
        sum=$(echo "$sum+$term" | bc)
    done
    echo "Sum of series = $sum"
}

sum_digits() {
    read -p "Enter a number: " n
    sum=0
    while [ "$n" -gt 0 ]; do
        digit=$((n%10))
        sum=$((sum+digit))
        n=$((n/10))
    done
    echo "Sum of digits = $sum"
}

reverse_string() {
    read -p "Enter a string: " str
    rev=""
    len=${#str}
    for (( i=len-1; i>=0; i-- )); do
        rev="$rev${str:i:1}"
    done
    echo "Reversed string = $rev"
}

string_ops() {
    read -p "Enter a string: " str
    echo "1. Length  2. Substring  3. Find character location"
    read -p "Choose operation: " choice
    case $choice in
        1) echo "Length = ${#str}" ;;
        2)
           read -p "Start index: " s
           read -p "Length of substring: " l
           echo "Substring = ${str:s:l}"
           ;;
        3)
           read -p "Enter character to find: " ch
           pos=-1
           for (( i=0; i<${#str}; i++ )); do
               if [ "${str:i:1}" == "$ch" ]; then
                   pos=$i
                   break
               fi
           done
           if [ "$pos" -eq -1 ]; then
               echo "Character not found"
           else
               echo "First found at position (0-indexed): $pos"
           fi
           ;;
        *) echo "Invalid choice" ;;
    esac
}

# -------------------- CONTROL STRUCTURE MODULE --------------------

palindrome_check() {
    read -p "Enter a string: " str
    rev=$(echo "$str" | rev)
    if [ "$str" == "$rev" ]; then
        echo "$str is a palindrome"
    else
        echo "$str is NOT a palindrome"
    fi
}

prime_check() {
    read -p "Enter a number: " n
    if [ "$n" -lt 2 ]; then
        echo "$n is NOT prime"
        return
    fi
    is_prime=1
    for (( i=2; i*i<=n; i++ )); do
        if [ $((n%i)) -eq 0 ]; then
            is_prime=0
            break
        fi
    done
    if [ $is_prime -eq 1 ]; then
        echo "$n is prime"
    else
        echo "$n is NOT prime"
    fi
}

char_type_check() {
    read -p "Enter a single character: " ch
    if [[ $ch =~ [0-9] ]]; then
        echo "$ch is a digit"
    elif [[ $ch =~ [a-z] ]]; then
        echo "$ch is a lowercase letter"
    elif [[ $ch =~ [A-Z] ]]; then
        echo "$ch is an uppercase letter"
    else
        echo "$ch is not alphanumeric"
    fi
}

sort_numbers() {
    read -p "Enter numbers separated by space: " -a nums
    n=${#nums[@]}
    for (( i=0; i<n; i++ )); do
        for (( j=0; j<n-i-1; j++ )); do
            if [ "${nums[j]}" -gt "${nums[j+1]}" ]; then
                temp=${nums[j]}
                nums[j]=${nums[j+1]}
                nums[j+1]=$temp
            fi
        done
    done
    echo "Sorted numbers: ${nums[@]}"
}

file_type_check() {
    read -p "Enter file name/path: " fname
    if [ -f "$fname" ]; then
        echo "$fname is an ordinary (regular) file"
    elif [ -d "$fname" ]; then
        echo "$fname is a directory, not an ordinary file"
    else
        echo "$fname does not exist"
    fi
}

# -------------------- MAIN MENU --------------------

main_menu() {
    while true; do
        echo ""
        echo "===== MULTI-UTILITY SHELL TOOLKIT ====="
        echo "1. Arithmetic Tools"
        echo "2. Loop-Based Tools"
        echo "3. Control-Structure Tools"
        echo "0. Exit"
        read -p "Enter your choice: " main_choice

        case $main_choice in
            1) arithmetic_menu ;;
            2) loop_menu ;;
            3) control_menu ;;
            0) echo "Exiting... Goodbye!"; exit 0 ;;
            *) echo "Invalid choice, try again." ;;
        esac
    done
}

arithmetic_menu() {
    echo "--- Arithmetic Tools ---"
    echo "1. Sum/Product/Average of 4 numbers"
    echo "2. Simple Calculator"
    echo "3. Gross Salary Calculator"
    echo "4. Factorial"
    echo "5. Max/Min from entered numbers"
    read -p "Choose: " c
    case $c in
        1) sum_product_avg ;;
        2) calculator ;;
        3) gross_salary ;;
        4) factorial ;;
        5) max_min_args ;;
        *) echo "Invalid choice" ;;
    esac
}

loop_menu() {
    echo "--- Loop-Based Tools ---"
    echo "1. Sum of series (1+x+x^2+x^3+x^4)"
    echo "2. Sum of digits"
    echo "3. Reverse a string"
    echo "4. String operations"
    read -p "Choose: " c
    case $c in
        1) series_sum ;;
        2) sum_digits ;;
        3) reverse_string ;;
        4) string_ops ;;
        *) echo "Invalid choice" ;;
    esac
}

control_menu() {
    echo "--- Control-Structure Tools ---"
    echo "1. Palindrome check"
    echo "2. Prime check"
    echo "3. Character type check (upper/lower/digit)"
    echo "4. Sort numbers ascending"
    echo "5. Check if file is ordinary"
    read -p "Choose: " c
    case $c in
        1) palindrome_check ;;
        2) prime_check ;;
        3) char_type_check ;;
        4) sort_numbers ;;
        5) file_type_check ;;
        *) echo "Invalid choice" ;;
    esac
}

# -------------------- START --------------------
main_menu
