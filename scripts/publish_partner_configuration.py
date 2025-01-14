#!/usr/bin/env python3

import os
import random
import string
import sys
from google.cloud import pubsub_v1
import json

project_id = 'gcp-training-playground-405915'
topic = 'partner-configurations'

def generate_message() -> dict:
    sample = {
        "displayName": ''.join(random.choices(string.ascii_uppercase + string.digits, k=10)),
        "shortName": f"lp{random.randint(0, 100)}"
    }
    return sample

def publish(message: dict):
    publisher = pubsub_v1.PublisherClient()
    topic_name = f"projects/{project_id}/topics/{topic}"
    future = publisher.publish(topic_name, json.dumps(message).encode())
    future.result()

def usage():
    print(f"""
    usage: python {sys.argv[0]} <number of message>
    """)
    exit()

if __name__ == '__main__':
    total_message  = int(sys.argv[1]) if len(sys.argv) > 1 else 1
    for _ in range(total_message):
        publish(generate_message())
