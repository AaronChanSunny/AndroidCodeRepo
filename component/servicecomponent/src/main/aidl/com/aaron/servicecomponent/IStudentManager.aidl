// IStudentManager.aidl
package com.aaron.servicecomponent;

// Declare any non-default types here with import statements
import com.aaron.servicecomponent.Student;

interface IStudentManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addStudent(in Student student);

    List<Student> getStudent();
}
