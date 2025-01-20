#!/usr/bin/env python3

import os
import random
import string
import sys
from google.cloud import pubsub_v1
import json

project_id = 'gcp-training-playground-405915'
topic = 'partners-configurations'

def generate_message() -> dict:
    sample = {
        "displayName": ''.join(random.choices(string.ascii_uppercase + string.digits, k=10)),
        "shortName": f"lp{random.randint(0, 100)}",
        "tenant": "de"
    }
    return sample

def publish(project_id: str, topic_id: str, message: dict):
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

    if 'PUBSUB_PROJECT_ID' in os.environ:
        project_id = os.environ['PUBSUB_PROJECT_ID']

    for _ in range(total_message):
        publish(project_id, topic, generate_message())
