import java.util.Arrays;

public class InfiniteNumber {

    private int[] internalArray;

    public InfiniteNumber(Object inputObject) throws IllegalArgumentException {
        if (inputObject instanceof Integer) {
            int input = (int) inputObject;
            if (input < 0) {
                throw new IllegalArgumentException("Cannot handle negative data");
            }
            if (input == 0) {
                internalArray = new int[]{0};
            } else {
                String inputString = Integer.toString(input);
                internalArray = new int[inputString.length()];
                for (int i = 0; i < inputString.length(); i++) {
                    internalArray[i] = Character.getNumericValue(inputString.charAt(i));
                }
            }
        } else if (inputObject instanceof String) {
            String inputString = (String) inputObject;
            if (!inputString.matches("\\d+")) {
                throw new IllegalArgumentException("Value other than integers are not allowed");
            }
            internalArray = new int[inputString.length()];
            for (int i = 0; i < inputString.length(); i++) {
                internalArray[i] = Character.getNumericValue(inputString.charAt(i));
            }
        } else if (inputObject instanceof int[]) {
            int[] inputArray = (int[]) inputObject;
            internalArray = Arrays.copyOf(inputArray, inputArray.length);
        } else {
            throw new IllegalArgumentException("Constructor of InfiniteNumber does not support this data type: " + inputObject.getClass().getName());
        }
    }

    public int[] getInternalArray() {
        return internalArray;
    }

    public String getNumberAsString() {
        StringBuilder sb = new StringBuilder();
        for (int digit : internalArray) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public boolean findBiggerArray(InfiniteNumber infiniteNumber) {
        int[] arr1 = internalArray;
        int[] arr2 = infiniteNumber.getInternalArray();

        if (arr1.length > arr2.length) {
            return true;
        } else if (arr1.length == arr2.length) {
            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] > arr2[i]) {
                    return true;
                } else if (arr1[i] < arr2[i]) {
                    return false;
                }
            }
        }
        return false;
    }

    public InfiniteNumber addTwoNos(InfiniteNumber infiniteNumber) {
        int[] arr1 = internalArray;
        int[] arr2 = infiniteNumber.getInternalArray();
        int maxLength = Math.max(arr1.length, arr2.length);
        arr1 = padArray(arr1, maxLength);
        arr2 = padArray(arr2, maxLength);
        
        int[] ansArr = new int[maxLength + 1];
        int carry = 0;
        for (int i = maxLength - 1; i >= 0; i--) {
            int sum = arr1[i] + arr2[i] + carry;
            ansArr[i + 1] = sum % 10;
            carry = sum / 10;
        }
        ansArr[0] = carry;
        
        if (ansArr[0] == 0) {
            return new InfiniteNumber(Arrays.copyOfRange(ansArr, 1, ansArr.length));
        } else {
            return new InfiniteNumber(ansArr);
        }
    }

    public InfiniteNumber subTwoNos(InfiniteNumber infiniteNumber) {
        int[] arr1 = internalArray;
        int[] arr2 = infiniteNumber.getInternalArray();
        int maxLength = Math.max(arr1.length, arr2.length);
        arr1 = padArray(arr1, maxLength);
        arr2 = padArray(arr2, maxLength);
        
        int[] ansArr = new int[maxLength];
        int borrow = 0;
        for (int i = maxLength - 1; i >= 0; i--) {
            int diff = arr1[i] - borrow - arr2[i];
            if (diff < 0) {
                diff += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            ansArr[i] = diff;
        }
        
        return new InfiniteNumber(removePrecedingZeros(ansArr));
    }

    public InfiniteNumber mulTwoNos(InfiniteNumber infiniteNumber) {
        int[] arr1 = internalArray;
        int[] arr2 = infiniteNumber.getInternalArray();

        int m = arr1.length;
        int n = arr2.length;
        int[] result = new int[m + n];

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int mul = arr1[i] * arr2[j];
                int sum = mul + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;
            }
        }

        // Remove leading zeros
        int index = 0;
        while (index < result.length && result[index] == 0) {
            index++;
        }
        if (index == result.length) {
            return new InfiniteNumber(0);
        }
        return new InfiniteNumber(Arrays.copyOfRange(result, index, result.length));
    }

    public InfiniteNumber divTwoNos(InfiniteNumber infiniteNumber) {
        int[] num1 = internalArray;
        int[] num2 = infiniteNumber.getInternalArray();

        // Convert arrays to InfiniteNumber objects
        InfiniteNumber resultant_No = new InfiniteNumber(num1);
        InfiniteNumber divisor = new InfiniteNumber(num2);

        // Perform division through repeated subtraction
        int divCounter = 0;
        while (resultant_No.findBiggerArray(divisor)) {
            resultant_No = resultant_No.subTwoNos(divisor);
            divCounter++;
        }

        return new InfiniteNumber(divCounter);
    }

    // Helper method to remove preceding zeros from the array
    private int[] removePrecedingZeros(int[] arr) {
        int nonZeroIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                nonZeroIndex = i;
                break;
            }
        }
        return Arrays.copyOfRange(arr, nonZeroIndex, arr.length);
    }

    // Helper method to pad the array with leading zeros to match the given length
    private int[] padArray(int[] arr, int length) {
        if (arr.length >= length) {
            return arr;
        }
        int[] paddedArray = new int[length];
        System.arraycopy(arr, 0, paddedArray, length - arr.length, arr.length);
        return paddedArray;
    }

}