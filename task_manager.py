#!/usr/bin/env python3
"""
Task Manager CLI
A command-line application for managing daily tasks efficiently.
"""

import json
import os
import sys
from datetime import datetime


class TaskManager:
    def __init__(self, storage_file='tasks.json'):
        self.storage_file = storage_file
        self.task_list = self.retrieve_tasks()

    def retrieve_tasks(self):
        """Retrieve tasks from storage file"""
        if os.path.exists(self.storage_file):
            try:
                with open(self.storage_file, 'r') as file:
                    return json.load(file)
            except json.JSONDecodeError:
                return []
        return []

    def persist_tasks(self):
        """Persist tasks to storage file"""
        with open(self.storage_file, 'w') as file:
            json.dump(self.task_list, file, indent=4)

    def create_task(self, description):
        """Create a new task and add it to the list"""
        if not description or description.strip() == '':
            return False
        
        new_task = {
            'task_id': len(self.task_list) + 1,
            'description': description.strip(),
            'is_done': False,
            'timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        }
        self.task_list.append(new_task)
        self.persist_tasks()
        return True

    def display_all_tasks(self):
        """Display all tasks in the list"""
        if not self.task_list:
            return "Your task list is empty."
        
        result = []
        result.append("\n" + "-"*60)
        result.append("TASK MANAGER - ALL TASKS")
        result.append("-"*60)
        
        for item in self.task_list:
            marker = "✔" if item['is_done'] else "○"
            result.append(f"{item['task_id']}. [{marker}] {item['description']}")
            result.append(f"   Added on: {item['timestamp']}")
        
        result.append("-"*60 + "\n")
        return "\n".join(result)

    def mark_as_done(self, task_id):
        """Mark a specific task as completed"""
        for item in self.task_list:
            if item['task_id'] == task_id:
                item['is_done'] = True
                self.persist_tasks()
                return True
        return False

    def remove_task(self, task_id):
        """Remove a task from the list"""
        original_count = len(self.task_list)
        self.task_list = [item for item in self.task_list if item['task_id'] != task_id]
        
        if len(self.task_list) < original_count:
            # Reassign IDs to maintain sequence
            for index, item in enumerate(self.task_list):
                item['task_id'] = index + 1
            self.persist_tasks()
            return True
        return False

    def remove_all_tasks(self):
        """Remove all tasks from the list"""
        self.task_list = []
        self.persist_tasks()
        return True


def show_menu():
    """Show the main menu options"""
    print("\n" + "-"*60)
    print("TASK MANAGER - MAIN MENU")
    print("-"*60)
    print("1. Create New Task")
    print("2. Show All Tasks")
    print("3. Mark Task as Done")
    print("4. Remove Task")
    print("5. Remove All Tasks")
    print("6. Quit")
    print("-"*60)


def run_application():
    """Run the Task Manager application"""
    manager = TaskManager()
    
    while True:
        show_menu()
        user_input = input("\nSelect an option (1-6): ").strip()
        
        if user_input == '1':
            description = input("Enter task description: ").strip()
            if manager.create_task(description):
                print("✔ Task created successfully!")
            else:
                print("✘ Task creation failed. Description cannot be empty.")
        
        elif user_input == '2':
            print(manager.display_all_tasks())
        
        elif user_input == '3':
            try:
                task_id = int(input("Enter task ID to mark as done: "))
                if manager.mark_as_done(task_id):
                    print(f"✔ Task #{task_id} marked as done!")
                else:
                    print(f"✘ Task #{task_id} not found.")
            except ValueError:
                print("✘ Invalid input. Please enter a valid number.")
        
        elif user_input == '4':
            try:
                task_id = int(input("Enter task ID to remove: "))
                if manager.remove_task(task_id):
                    print(f"✔ Task #{task_id} removed successfully!")
                else:
                    print(f"✘ Task #{task_id} not found.")
            except ValueError:
                print("✘ Invalid input. Please enter a valid number.")
        
        elif user_input == '5':
            confirmation = input("Are you sure you want to remove all tasks? (yes/no): ").lower()
            if confirmation == 'yes':
                manager.remove_all_tasks()
                print("✔ All tasks have been removed!")
            else:
                print("Action cancelled.")
        
        elif user_input == '6':
            print("\nThank you for using Task Manager!")
            sys.exit(0)
        
        else:
            print("✘ Invalid option. Please select a number between 1-6.")


if __name__ == "__main__":
    run_application()
