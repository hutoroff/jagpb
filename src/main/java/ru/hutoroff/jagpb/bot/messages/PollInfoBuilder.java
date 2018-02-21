package ru.hutoroff.jagpb.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.model.Voter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PollInfoBuilder {

	public static SendMessage buildPollMessage(PollDO poll, Long chatId) {
		final String text = prepareText(poll);
		final SendMessage result = new SendMessage(chatId, text);

		if (poll.getOptions() != null) {
			result.setReplyMarkup(prepareKeybaord(poll.getOptions(), poll.getId().toString()));
		}

		return result;
	}

	public static InlineKeyboardMarkup prepareKeybaord(List<PollOption> options, String pollId) {
		final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(options.size());

		for (PollOption option : options) {
			InlineKeyboardButton button = new InlineKeyboardButton(option.getTitle());
			button.setCallbackData(String.format("vote_%s_%s", pollId, option.getId()));
			keyboard.add(Collections.singletonList(button));
		}
		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	public static String prepareText(PollDO poll) {
		StringBuilder sb = new StringBuilder(poll.getTitle()).append("\n\n");

		if (poll.getOptions() != null) {
			sb.append("Options:");
			for (PollOption pollOption : poll.getOptions()) {
				final Set<Voter> voters = pollOption.getVoters() != null ? pollOption.getVoters() : Collections.emptySet();
				sb.append("\n* ").append(pollOption.getTitle()).append(" -- Voted: ").append(voters.size());
				for (Voter voter : voters) {
					sb.append("\n\t-").append(voter.getFirstName()).append(" ").append(voter.getLastName()).append(" (")
							.append(voter.getUsername()).append(")");
				}
			}
		}

		return sb.toString();
	}
}
