#!/usr/bin/env python3
"""
Unit tests for Task Manager application
"""

import unittest
import json
import os
import sys

# Add parent directory to path to import task_manager
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from task_manager import TaskManager


class TestTaskManager(unittest.TestCase):
    
    def setUp(self):
        """Set up test fixtures"""
        self.test_file = 'test_tasks.json'
        self.manager = TaskManager(self.test_file)
        
    def tearDown(self):
        """Clean up test files"""
        if os.path.exists(self.test_file):
            os.remove(self.test_file)
    
    def test_create_task(self):
        """Test creating a new task"""
        result = self.manager.create_task("Test task")
        self.assertTrue(result)
        self.assertEqual(len(self.manager.task_list), 1)
        self.assertEqual(self.manager.task_list[0]['description'], "Test task")
    
    def test_create_empty_task(self):
        """Test creating an empty task should fail"""
        result = self.manager.create_task("")
        self.assertFalse(result)
        self.assertEqual(len(self.manager.task_list), 0)
    
    def test_mark_as_done(self):
        """Test marking a task as done"""
        self.manager.create_task("Test task")
        result = self.manager.mark_as_done(1)
        self.assertTrue(result)
        self.assertTrue(self.manager.task_list[0]['is_done'])
    
    def test_remove_task(self):
        """Test removing a task"""
        self.manager.create_task("Test task 1")
        self.manager.create_task("Test task 2")
        result = self.manager.remove_task(1)
        self.assertTrue(result)
        self.assertEqual(len(self.manager.task_list), 1)
        self.assertEqual(self.manager.task_list[0]['description'], "Test task 2")
    
    def test_remove_all_tasks(self):
        """Test removing all tasks"""
        self.manager.create_task("Test task 1")
        self.manager.create_task("Test task 2")
        result = self.manager.remove_all_tasks()
        self.assertTrue(result)
        self.assertEqual(len(self.manager.task_list), 0)
    
    def test_persist_and_retrieve(self):
        """Test saving and loading tasks"""
        self.manager.create_task("Test task")
        new_manager = TaskManager(self.test_file)
        self.assertEqual(len(new_manager.task_list), 1)
        self.assertEqual(new_manager.task_list[0]['description'], "Test task")


if __name__ == '__main__':
    unittest.main()
