#!/usr/bin/env python3

import sys
from google.cloud import pubsub_v1
from google.api_core.exceptions import AlreadyExists


def create_topic(project_id: str, topic_id: str) -> None:
    publisher = pubsub_v1.PublisherClient()
    topic_path = publisher.topic_path(project_id, topic_id)
    topic = publisher.create_topic(request={"name": topic_path})
    print(f"Created topic: {topic.name}")


def create_subscription(project_id: str, topic_id: str, subscription_id: str) -> None:
    publisher = pubsub_v1.PublisherClient()
    subscriber = pubsub_v1.SubscriberClient()
    topic_path = publisher.topic_path(project_id, topic_id)
    subscription_path = subscriber.subscription_path(project_id, subscription_id)

    with subscriber:
        subscription = subscriber.create_subscription(
            request={"name": subscription_path, "topic": topic_path}
        )

    print(f"Subscription created: {subscription}")


if __name__ == '__main__':
    if len(sys.argv) != 4:
        print(f"usage: python {sys.argv[0]} <project_id> <topic_name> <subscription_name>")
        exit()

    project_id = sys.argv[1]
    topic_id = sys.argv[2]
    subscription_id = sys.argv[3]

    try:
        create_topic(project_id, topic_id)
        create_subscription(project_id, topic_id, subscription_id)
    except AlreadyExists:
        pass
